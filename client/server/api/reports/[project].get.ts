import sonarQubeReport from '../../data/age-verification-app.json'
import semgrepReport from '../../data/semgrep-results.json'

import {SonarQubeIssueMapper} from "~/mappers/sonar-qube-issue.mapper";
import {SemgrepIssueMapper} from "~/mappers/semgrep-issue.mapper";

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
            }
        ]
    }

    throw createError({
        statusCode: 404,
        statusMessage: 'Project not found'
    })
})
