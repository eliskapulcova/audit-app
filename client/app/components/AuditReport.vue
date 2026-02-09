<template>
  <div class="report mx-auto my-3 max-w-5xl">
    <h1 class="text-3xl font-bold text-center m-5 mx-auto">
      <span v-if="isLatest">Latest</span> {{ report.reportTool }} report for
      {{ projectName }}
    </h1>
    <AnalysisReport :report="report.analysisReport" />
    <IssueReport :issues="report.issueList" />
  </div>
</template>

<script setup lang="ts">
import type { Report } from '~/domain/report'

const props = defineProps<{
  projectName: string
  report: Report
  isLatest: boolean
}>()
</script>
