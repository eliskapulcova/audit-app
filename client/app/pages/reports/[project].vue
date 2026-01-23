<template>
    <h1 class="text-3xl font-bold text-center m-5 mx-auto">{{ route.params.project }}</h1>
    <p v-if="pending">Loading...</p>
    <p v-else-if="error">Error: {{ error }}</p>
    <div v-else-if="data" class="flex flex-col lg:flex-row">
        <AnalysisReport :report="data.analysisReport.data"/>
        <IssueReport :issues="data.issueReport.auditIssueList" />
    </div>
</template>

<script setup lang="ts">
const route = useRoute();

const { data, pending, error } = await useFetch(`/api/reports/${route.params.project}`);
</script>