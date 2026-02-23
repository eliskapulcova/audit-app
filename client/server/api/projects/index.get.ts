import type { ProjectDetails } from '~/domain/types'

import {
  healthScore,
  kpis,
  sonarQubeData,
  semgrepData,
  phpcsData,
  phpstanData,
  trendData,
  topIssues,
  repositoryHealth,
  projectSummaries,
} from '~/mock/v2/mock-data'


export default defineEventHandler(
  async (): Promise<{ data: ProjectDetails[]}> => {
    return { data: projectSummaries.map((p) => {
      return {
        healthScore,
        kpis,
        sonarQubeData,
        semgrepData,
        phpcsData,
        phpstanData,
        trendData,
        topIssues,
        repositoryHealth,
        projectSummary: p,
      }
    })}
  }
)
