import type { SemgrepIssue } from '../domain/semgrep-issue';
import type { AuditIssue } from '../domain/audit-issue';

export class SemgrepIssueMapper {
    static mapIssue(issue: SemgrepIssue): AuditIssue {
        console.log('mapping issue', issue);
        return {
            component: issue.path,
            file: `${issue.path.substring(issue.path.lastIndexOf('/') + 1)}:${issue.start.line}`,
            location: issue.path.slice(issue.path.indexOf('/') + 1, issue.path.lastIndexOf('/')),
            fileType:  issue.path?.match(/\.([^.]*)$/)?.[1] ?? '',
            author: null,
            rule: issue.extra?.metadata?.cwe?.[0]?.split(':')?.[0] ?? '',
            ruleDocLink: issue.extra?.metadata?.source ?? '',
            severity: issue.extra.severity,
            type: null,
            impacts: { softwareQuality: issue.extra?.metadata?.category, severity: issue.extra?.metadata?.impact },
            tags: [],
            message: issue.extra.message,
            quickFixAvailable: !!issue.extra.fix,
            effort: null,
        }
    }
}
