

import React, { useState } from "react";
import { Card, Header } from "@egovernments/digit-ui-react-components";
import RtgsIssueRegisterNewForm from "../../../../components/reports/RtgsIssueRegisterNewForm";
import VoucherStatusReportBeforeSearchForm from "../../../../components/reports/VoucherStatusReportBeforeSearchForm";

const RtgsIssueRegisterReportNewForm = () => {
  const [filters, setFilters] = useState(null);

  const handleSubmit = (formData) => {
    setFilters(formData);
  };

  const handleReset = () => {
    setFilters(null);
  };

  return (
    <div style={{ padding: "16px" }}>
      {/* <Header>Cheque Issue Register Report</Header> */}
          <Card
        style={{
          fontFamily: "semibold", // Changed hyphen to camelCase
          fontSize: "16px",
          fontWeight: "bold",
          color: "#fff",
          background: "#34495E",
          borderColor: "#337ab7 !important", // Remove !important as it doesn't work in inline styles
          borderLeft: "5px solid #337ab7", // Added color for borderLeft
          borderBottom: "1px solid #337ab7", // Added color for borderBottom
          textAlign: "left", // Changed to camelCase

          boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
          padding: "8px 15px", // Uncommented and standardized
        }}
      >
        <div>
          <h1>📊RTGS Issue Report</h1>
        </div>
      </Card>
      
      <RtgsIssueRegisterNewForm
        onSubmit={handleSubmit} 
        onReset={handleReset} 
      />
      
     {/* <ChequeIssueReportTable filters={filters} /> */}
    </div>
  );
};

export default RtgsIssueRegisterReportNewForm;