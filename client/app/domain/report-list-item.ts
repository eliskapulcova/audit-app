import type { ReportTool } from "./report-tool";

export interface ReportListItem {
    projectName: string;
    availableReports: ReportMeta[]
}

interface ReportMeta {
    reportTool: ReportTool;
    createdAt: Date;
}
