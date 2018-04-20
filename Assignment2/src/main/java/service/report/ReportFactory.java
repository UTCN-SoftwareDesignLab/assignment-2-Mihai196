package service.report;

import org.springframework.stereotype.Service;
import service.report.CSVReportService;
import service.report.PDFReportService;
import service.report.ReportService;

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
