import type { AuditIssue } from "./audit-issue"
import type { ReportTool } from "./report-tool"

export interface Report {
    reportTool: ReportTool
    id?: string
    date?: Date
    triggeredBy?: string
    branch?: string
    commitHash?: string
    analysisReport?: any // TODO: Add proper type
    issueList: AuditIssue[]
}
