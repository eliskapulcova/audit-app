import sonarQubeReport from '../../../../../data/age-verification-app.json'

import { SonarQubeIssueMapper } from '~/mappers/sonar-qube-issue.mapper'
import { Report } from '~/domain/report'
import { ReportTool } from '~/domain/report-tool'

export default defineEventHandler(async (): Promise<Report> => {
  return {
    reportTool: ReportTool.SonarQube,
    analysisReport: sonarQubeReport.analysisReport.data,
    issueList: sonarQubeReport.issueReport.auditIssueList.map(SonarQubeIssueMapper.mapIssue),
  }
})