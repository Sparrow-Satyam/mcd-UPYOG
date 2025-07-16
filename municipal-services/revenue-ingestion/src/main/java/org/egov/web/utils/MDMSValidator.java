package org.egov.web.utils;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.egov.web.contract.IngestionRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MDMSValidator {

    /**
     * method to validate the mdms data in the request
     *
     * @param assetRequest
     */
    public void validateMdmsData(IngestionRequest assetRequest, Object mdmsData) {

        Map<String, List<String>> masterData = getAttributeValues(mdmsData);
        String[] masterArray = {IngestionConstants.FINANCE_GL_CODE_MAPPING};
        validateIfMasterPresent(masterArray, masterData);
    }


    /**
     * Fetches all the values of particular attribute as map of field name to
     * list
     * <p>
     * takes all the masters from each module and adds them in to a single map
     * <p>
     * note : if two masters from different modules have the same name then it
     * <p>
     * will lead to overriding of the earlier one by the latest one added to the
     * map
     *
     * @return Map of MasterData name to the list of code in the MasterData
     */
    public Map<String, List<String>> getAttributeValues(Object mdmsData) {

        List<String> modulepaths = Collections.singletonList(IngestionConstants.MODULE_JSONPATH_CODE);
        final Map<String, List<String>> mdmsResMap = new HashMap<>();
        modulepaths.forEach(modulepath -> {
            try {
                mdmsResMap.putAll(JsonPath.read(mdmsData, modulepath));
            } catch (Exception e) {
                throw new CustomException(IngestionErrorConstants.INVALID_TENANT_ID_MDMS_KEY,
                        IngestionErrorConstants.INVALID_TENANT_ID_MDMS_MSG);
            }
        });
        return mdmsResMap;
    }

    /**
     * Validates if MasterData is properly fetched for the given MasterData
     * names
     *
     * @param masterNames
     * @param codes
     */
    private void validateIfMasterPresent(String[] masterNames, Map<String, List<String>> codes) {
        Map<String, String> errorMap = new HashMap<>();
        for (String masterName : masterNames) {
            if (CollectionUtils.isEmpty(codes.get(masterName))) {
                errorMap.put("MDMS DATA ERROR ", "Unable to fetch " + masterName + " codes from MDMS");
            }
        }
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }

}
