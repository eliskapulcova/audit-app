import type { SemgrepIssue } from '../domain/issue_mapping/semgrep-issue';
import type { AuditIssue } from '../domain/audit-issue';
import { ReportTool } from '../domain/report-tool';

export class SemgrepIssueMapper {
    static mapIssue(issue: SemgrepIssue): AuditIssue {
        return {
            tool: ReportTool.Semgrep,
            location: issue.path.replace('/', ':').slice(0, issue.path.lastIndexOf('/')),
            file: issue.path.substring(issue.path.lastIndexOf('/') + 1),
            fileType:  issue.path?.match(/\.([^.]*)$/)?.[1] ?? '',
            line: issue.start.line,
            author: null,
            rule: issue.extra?.metadata?.cwe?.[0]?.split(':')?.[0] ?? '',
            ruleDocLink: issue.extra?.metadata?.source ?? '',
            severity: issue.extra.severity,
            issueType: null,
            impacts: { softwareQuality: issue.extra?.metadata?.category, severity: issue.extra?.metadata?.impact },
            tags: [],
            message: issue.extra.message,
            quickFixAvailable: !!issue.extra.fix,
            effort: null,
        }
    }
}
