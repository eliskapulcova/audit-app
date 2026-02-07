import sonarQubeReport from '../../../../../data/age-verification-app.json'

import { SonarQubeIssueMapper } from '~/mappers/sonar-qube-issue.mapper'
import { Report } from '~/domain/report'
import { ReportTool } from '~/domain/report-tool'

export default defineEventHandler(async (): Promise<Report[]> => {
  return [{
    reportTool: ReportTool.SonarQube,
    id: '1',
    date: new Date('2026-01-06T10:53:00'),
    triggeredBy: 'someone',
    branch: 'main',
    commitHash: 'abcdef1234567890',
    analysisReport: sonarQubeReport.analysisReport.data,
    issueList: sonarQubeReport.issueReport.auditIssueList.map(SonarQubeIssueMapper.mapIssue),
  }, {
    reportTool: ReportTool.SonarQube,
    id: '2',
    date: new Date('2026-02-05T10:53:00'),
    triggeredBy: 'someone else',
    branch: 'main',
    commitHash: 'abcdef1234567890',
    analysisReport: sonarQubeReport.analysisReport.data,
    issueList: sonarQubeReport.issueReport.auditIssueList.map(SonarQubeIssueMapper.mapIssue),
  }]
})