import { projectSummaries } from '~/mock/v2/mock-data'
import type { ProjectSummary } from '~/domain/types'

export default defineEventHandler(
  async (): Promise<{ projectSummaries: ProjectSummary[] }> => {
    return { projectSummaries }
  }
)