import type { PhpstanIssue } from '../domain/_obsolete/issue_mapping/phpstan-issue';
import type { AuditIssue } from '../domain/_obsolete/audit-issue';
import { ReportTool } from '../domain/_obsolete/report-tool';

export class PhpstanIssueMapper {
    static mapIssue(issue: PhpstanIssue, appName: string, path: string): AuditIssue {
        const transformedPath = path.replace('/app/', `${appName}:`);
        const lastSlashIndex = transformedPath.lastIndexOf('/');
        return {
            tool: ReportTool.PHPStan,
            location: transformedPath.slice(0, lastSlashIndex), 
            file: transformedPath.substring(lastSlashIndex + 1),
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
