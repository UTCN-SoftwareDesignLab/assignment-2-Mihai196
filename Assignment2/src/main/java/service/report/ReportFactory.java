package service.report;

import org.springframework.stereotype.Service;

@Service
public class ReportFactory {
    public ReportService getReportType(String reportType)
    {
        if(reportType.equals("CSV"))
        {
            return new CSVReportService();
        }
        if(reportType.equals("PDF"))
        {
            return new PDFReportService();
        }
        return null;

    }
}
