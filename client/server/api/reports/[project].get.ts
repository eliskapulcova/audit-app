import sonarQubeReport from '../../data/age-verification-app.json'
import semgrepReport from '../../data/semgrep-results.json'
import phpstanReport from '../../data/phpstan-report.json'

import {SonarQubeIssueMapper} from "~/mappers/sonar-qube-issue.mapper";
import {SemgrepIssueMapper} from "~/mappers/semgrep-issue.mapper";
import {PhpstanIssueMapper} from "~/mappers/phpstan-issue-mapper";
import { PhpstanIssue } from '~/domain/issue_mapping/phpstan-issue';

export default defineEventHandler((event) => {
    const project = getRouterParam(event, 'project')

    if (project === 'age-verification-app') {
        return [
            {
                reportTool: 'SonarQube',
                analysisReport: sonarQubeReport.analysisReport.data,
                issueList: sonarQubeReport.issueReport.auditIssueList.map(SonarQubeIssueMapper.mapIssue),
            },
            {
                reportTool: 'Semgrep',
                issueList: semgrepReport.results.map(SemgrepIssueMapper.mapIssue),
                analysisReport: null,
            },
            {
                reportTool: 'PHPStan',
                issueList: Object.entries(phpstanReport.files)
                    .flatMap(([path, file]) => file.messages.map(
                        (issue: PhpstanIssue) => PhpstanIssueMapper.mapIssue(issue, 'age-verification', path.split(/\s+/)[0]))),
                analysisReport: null,
            }
        ]
    }

    throw createError({
        statusCode: 404,
        statusMessage: 'Project not found'
    })
})
