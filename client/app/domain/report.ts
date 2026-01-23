import type { AuditIssue } from "./audit-issue"

export interface Report {
    analysisReport: any // TODO: Add proper type
    issueList: AuditIssue[]
}
