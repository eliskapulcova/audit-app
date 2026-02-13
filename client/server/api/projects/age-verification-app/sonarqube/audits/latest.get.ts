import sonarQubeReport from '../../../../../data/age-verification-app.json'

import { SonarQubeIssueMapper } from '~/mappers/sonar-qube-issue.mapper'
import { Report } from '~/domain/_obsolete/report'
import { ReportTool } from '~/domain/_obsolete/report-tool'

export default defineEventHandler(async (): Promise<Report> => {
  return {
    reportTool: ReportTool.SonarQube,
    analysisReport: sonarQubeReport.analysisReport.data,
    issueList: sonarQubeReport.issueReport.auditIssueList.map(SonarQubeIssueMapper.mapIssue),
  }
})