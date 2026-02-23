import { dashboardData } from '~/mock/v2/mock-data'
import type { DashboardData } from '~/domain/types'

export default defineEventHandler(
  async (): Promise<DashboardData> => {
    return dashboardData
  }
)