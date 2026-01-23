/**
 * TODO: Add proper types instead of 'any' for the issue and the return type.
 */
import type { AuditIssue } from '../domain/audit-issue';
import type { SonarQubeIssue } from '../domain/issue_mapping/sonar-qube-issue';

export class SonarQubeIssueMapper {
    static mapIssue(issue: SonarQubeIssue): AuditIssue {
        return {
            source: 'SonarQube',
            component: issue.component,
            file: `${issue.component?.match(/[^:/]*$/)?.[0] ?? ''}:${issue.line}`,
            location: issue.component?.replace(/[^:/]*$/, '').replace(/[:/]$/, '') ?? '',
            fileType:  issue.component?.match(/\.([^.]*)$/)?.[1] ?? '',
            author: issue.author,
            rule: issue.rule,
            ruleDocLink: `https://next.sonarqube.com/sonarqube/coding_rules?open=${encodeURIComponent(issue.rule)}&rule_key=${encodeURIComponent(issue.rule)}`,
            severity: issue.severity,
            type: issue.type,
            impacts: Object.fromEntries(issue.impacts.replace(/[{}\/]/g, '').split(', ').map((impact) => impact.split('='))),
            tags: issue.tags.split(' / ').filter(tag => tag !== ''),
            message: issue.message,
            quickFixAvailable: issue.quickFixAvailable,
            effort: issue.effort.replace(/^(\d+)(min)$/, "$1 $2."),
        }
    }
}
