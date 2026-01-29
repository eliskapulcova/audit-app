import type { PhpstanIssue } from '../domain/issue_mapping/phpstan-issue';
import type { AuditIssue } from '../domain/audit-issue';
import { ReportTool } from '../domain/report-tool';

export class PhpstanIssueMapper {
    static mapIssue(issue: PhpstanIssue, appName: string, path: string): AuditIssue {
        return {
            tool: ReportTool.PHPStan,
            location: path.replace('/app/', `${appName}:`).slice(0, path.lastIndexOf('/')), 
            file: path.replace('/app/', `${appName}:`).substring(path.lastIndexOf('/') + 1),
            fileType: path?.match(/\.(.*)$/)?.[1] ?? '',
            line: issue.line,
            author: null,
            rule: issue.identifier,
            ruleDocLink: issue.tip?.substring(issue.tip.indexOf('http')) ?? null,
            severity: issue.ignorable ? 'WARNING' : 'ERROR',
            issueType: null,
            impacts: {},
            tags: [],
            message: issue.message,
            quickFixAvailable: false,
            effort: null,
        }
    }
}
