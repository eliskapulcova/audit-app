import type { ReportTool } from "./report-tool";
import type { RepositoryType } from "./repository-type";

export interface ReportListItem {
    projectName: string
    repositoryUrl?: string
    repositoryType?: RepositoryType
    branch?: string
    latestReportSet: ReportSet | null
    allReportSets: ReportSet[]
}

interface ReportMeta {
    reportTool: ReportTool
    createdAt: Date
}

interface ReportSet {
    createdAt: Date
    availableReports: ReportMeta[]
}
