import type { ProjectSummary } from '~/domain/types'
import { projectSummaries } from '~/mock/v2/mock-data'

export default defineEventHandler(
  async (event): Promise<ProjectSummary> => {
    const projectKey = getRouterParam(event, 'project_key')
    const projectSummary = projectSummaries.find((p) => p.id === projectKey)

    if (!projectSummary) {
      throw createError({
        statusCode: 404,
        statusMessage: 'Project ' + projectKey + ' not found',
      })
    }

    return projectSummary
  }
)