<template>
  <div class="space-y-6" v-if="data">
    <TopIssuesTable :issues="data.topIssues" />

    <footer
      class="text-center text-sm text-slate-500 pt-8 border-t border-slate-800"
    >
      Quanti Code Quality Hub v1.0 — Data refreshed daily
    </footer>
  </div>
</template>

<script setup lang="ts">
import TopIssuesTable from '~/components/TopIssuesTable.vue'
import type { ProjectDetails } from '~/domain/types'

definePageMeta({
  layout: 'v2',
})

const route = useRoute()

// TODO: Handle pending and error states
const { data, pending, error } = await useFetch<ProjectDetails>(
  '/api/v2/project/' + route.params.project + '/repository/' + route.params.repository
)
</script>
