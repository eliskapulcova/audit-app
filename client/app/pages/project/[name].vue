<template>
  <div class="space-y-6" v-if="data">
    <HealthScore
      :health-score="data.healthScore"
      :project-summary="data.projectSummary"
    />
    <KPICards :kpis="data.kpis" />

    <div>
      <h2 class="text-2xl font-bold text-white mb-4">Tool Results</h2>
      <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
        <SonarQubeCard v-if="data.sonarQubeData" :data="data.sonarQubeData" />
        <SemgrepCard v-if="data.semgrepData" :data="data.semgrepData" />
        <PHPCSCard v-if="data.phpcsData" :data="data.phpcsData" />
        <PHPStanCard v-if="data.phpstanData" :data="data.phpstanData" />
        <!-- <AddToolCard /> -->
      </div>
    </div>

    <TrendChart :data="data.trendData" />
    <TopIssuesTable :issues="data.topIssues" />
    <RepositoryHealthMatrix
      :repositories="data.repositoryHealth"
      :tools="data.projectSummary.tools"
    />

    <footer
      class="text-center text-sm text-slate-500 pt-8 border-t border-slate-800"
    >
      Quanti Code Quality Hub v1.0 — Data refreshed daily
    </footer>
  </div>
  <div v-else-if="pending">
    <p>Loading project details...</p>
  </div>
  <div v-else-if="error">
    <p v-if="error.message.includes('404')" class="text-red-500">
      Project not found
    </p>
    <p v-else class="text-red-500">Error: {{ error.message }}</p>
  </div>
</template>

<script setup lang="ts">
import HealthScore from '~/components/HealthScore.vue'
import KPICards from '~/components/KPICards.vue'
import SonarQubeCard from '~/components/audit-tools/SonarQubeCard.vue'
import SemgrepCard from '~/components/audit-tools/SemgrepCard.vue'
import PHPCSCard from '~/components/audit-tools/PHPCSCard.vue'
import PHPStanCard from '~/components/audit-tools/PHPStanCard.vue'
import AddToolCard from '~/components/AddToolCard.vue'
import TrendChart from '~/components/TrendChart.vue'
import TopIssuesTable from '~/components/TopIssuesTable.vue'
import RepositoryHealthMatrix from '~/components/RepositoryHealthMatrix.vue'
import type { ProjectDetails } from '~/domain/types'

definePageMeta({
  layout: 'v2',
})

const route = useRoute()

const useMockData = route.params.name !== 'age_verification'

// TODO: Handle pending and error states
const { data, pending, error } = await useFetch<ProjectDetails>(
  useMockData
    ? '/api/v2/project/' + route.params.name
    : 'http://localhost:8080/details?projectKey=age_verification'
)
</script>
