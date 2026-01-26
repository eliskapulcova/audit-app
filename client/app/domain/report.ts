import type { AuditIssue } from "./audit-issue"
import type { ReportTool } from "./report-tool"

export interface Report {
    reportTool: ReportTool
    analysisReport?: any // TODO: Add proper type
    issueList: AuditIssue[]
}
