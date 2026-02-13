<template>
  <ToolCard
    title="SonarQube"
    icon="lucide:database"
    :status="data.healthStatus"
    :last-run="data.lastRun"
  >
  <div class="flex flex-row flex-wrap gap-4">
    <TotalFindings class="flex-1" :total-violations="totalIssues" title="Total Findings" />
    <div v-if="data.technicalDebt" class="flex-1 bg-slate-900 p-4 rounded-lg">
      <div class="text-3xl font-bold text-white mb-1">
        {{ data.technicalDebt }}
      </div>
      <div class="text-sm text-slate-400">Technical debt</div>
    </div>
  </div>

  <div class="flex flex-row flex-wrap gap-4">
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-red-500">
        <div class="text-2xl font-bold text-red-400">{{ data.severityBreakdown.blocker }}</div>
        <div class="text-xs text-slate-400">Blocker</div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-orange-500">
        <div class="text-2xl font-bold text-orange-400">{{ data.severityBreakdown.critical }}</div>
        <div class="text-xs text-slate-400">Critical</div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-yellow-500">
        <div class="text-2xl font-bold text-yellow-400">{{ data.severityBreakdown.major }}</div>
        <div class="text-xs text-slate-400">Major</div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-blue-500">
        <div class="text-2xl font-bold text-blue-400">{{ data.severityBreakdown.minor }}</div>
        <div class="text-xs text-slate-400">Minor</div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg border-green-500">
        <div class="text-2xl font-bold text-green-400">{{ data.severityBreakdown.info }}</div>
        <div class="text-xs text-slate-400">Info</div>
      </div>
    </div>

    <div class="flex flex-row flex-wrap gap-4">
      <div class="flex-1 bg-slate-900 p-3 rounded-lg">
        <div class="flex flex-row gap-4 justify-between items-center">
            <div>
              <div class="text-2xl font-bold text-white">{{ data.bugs }}</div>
              <div class="text-xs text-slate-400">Bugs</div>
            </div>
            <Icon name="lucide:bug" class="w-6 h-6 text-white" />
        </div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg">
        <div class="flex flex-row gap-4 justify-between items-center">
            <div>
              <div class="text-2xl font-bold text-white">
                {{ data.vulnerabilities }}
              </div>
              <div class="text-xs text-slate-400">Vulnerabilities</div>
            </div>
            <Icon name="lucide:shield-alert" class="w-6 h-6 text-white" />
        </div>
      </div>
      <div class="flex-1 bg-slate-900 p-3 rounded-lg">
        <div class="flex flex-row gap-4 justify-between items-center">
            <div>
              <div class="text-2xl font-bold text-white">
                {{ data.codeSmells }}
              </div>
              <div class="text-xs text-slate-400">Code Smells</div>
            </div>
            <Icon name="lucide:code" class="w-6 h-6 text-white" />
        </div>
      </div>
    </div>

    <FindingsTrend v-if="data.trendData.length >= TREND_DATA_MIN_POINTS" :trend-data="data.trendData" />

    <div class="flex flex-row flex-wrap gap-4">
      <div v-if="data.coverage != null" class="flex-1 bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-green-400">{{ data.coverage }}%</div>
        <div class="text-xs text-slate-400">Coverage</div>
      </div>
      <div v-if="data.duplications != null" class="flex-1 bg-slate-900 p-3 rounded-lg">
        <div class="text-xl font-bold text-blue-400">
          {{ data.duplications }}%
        </div>
        <div class="text-xs text-slate-400">Duplications</div>
      </div>
    </div>

    <div class="bg-slate-900 p-3 rounded-lg">
      <div class="flex items-center justify-between mb-4">
        <span class="text-lg text-slate-400">Quality Gate</span>
        <span
          v-if="data.qualityGate"
          :class="[
            'text-lg font-bold',
            data.qualityGate === 'Passed' ? 'text-green-400' : 'text-red-400',
          ]"
        >
          {{ data.qualityGate }}
        </span>
      </div>
      <div class="flex flex-row flex-wrap gap-4 text-sm">
        <div class="flex items-center gap-2">
          <div
            v-if="data.reliability"
            :class="[
              'w-8 h-8 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.reliability),
            ]"
          >
            {{ data.reliability }}
          </div>
          <span class="text-slate-400 text-base">Reliability</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            v-if="data.security"
            :class="[
              'w-8 h-8 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.security),
            ]"
          >
            {{ data.security }}
          </div>
          <span class="text-slate-400 text-base">Security</span>
        </div>
        <div class="flex items-center gap-2">
          <div
            v-if="data.maintainability"
            :class="[
              'w-8 h-8 rounded flex items-center justify-center text-xs font-bold text-white',
              getRatingColor(data.maintainability),
            ]"
          >
            {{ data.maintainability }}
          </div>
          <span class="text-slate-400 text-base">Maintainability</span>
        </div>
      </div>
    </div>

    <TopFiles v-if="data.topFiles.length > 0" title="Files with Most Violations" :data="data.topFiles" />
  </ToolCard>
</template>

<script setup lang="ts">
import ToolCard from '../ToolCard.vue'
import TotalFindings from './details/TotalFindings.vue'
import FindingsTrend from './details/FindingsTrend.vue'
import TopFiles from './details/TopFiles.vue'
import type { SonarQubeData } from '../../../domain/types'
import { TREND_DATA_MIN_POINTS } from '../../../config/general'

const props = defineProps<{
  data: SonarQubeData
}>()

const totalIssues = computed(
  () => props.data.bugs + props.data.vulnerabilities + props.data.codeSmells
)

const totalSeverity = computed(
  () =>
    props.data.severityBreakdown.blocker +
    props.data.severityBreakdown.critical +
    props.data.severityBreakdown.major +
    props.data.severityBreakdown.minor +
    props.data.severityBreakdown.info
)

const hasQualityGateData = computed(() => {
  return (
    props.data.qualityGate !== null ||
    props.data.reliability !== null ||
    props.data.security !== null ||
    props.data.maintainability !== null
  )
})

const getRatingColor = (rating: string) => {
  switch (rating) {
    case 'A':
      return 'bg-green-500'
    case 'B':
      return 'bg-blue-500'
    case 'C':
      return 'bg-yellow-500'
    case 'D':
      return 'bg-orange-500'
    case 'E':
      return 'bg-red-500'
    default:
      return 'bg-slate-500'
  }
}
</script>
