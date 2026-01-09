<template>
    <DataTable :value="processedIssues" responsiveLayout="scroll">
        <Column field="fileType" header="File Type"></Column>
        <Column field="location" header="Location"></Column>
        <Column field="rule" header="Rule"></Column>
        <Column field="message" header="Message"></Column>
        <Column field="severity" header="Severity"></Column>
        <Column field="type" header="Type"></Column>
        <Column field="impacts" header="Impacts"></Column>
        <Column field="tags" header="Tags"></Column>
        <Column field="quickFixAvailable" header="Quick Fix Available"></Column>
        <Column field="effort" header="Effort"></Column>
        <Column field="author" header="Author"></Column>
    </DataTable>
</template>

<script setup>
import { issues } from '~/mock/issues-report';

const processedIssues = computed(() => {
    return issues.map(issue => {
        return {
            location: `${issue.component?.split(':')?.pop()}:${issue.line}`,
            fileType: issue.component?.split('.')?.pop(),
            author: issue.author,
            rule: issue.rule,
            severity: issue.severity,
            type: issue.type,
            impacts: Object.fromEntries(issue.impacts.split(', ').map((impact) => impact.split('='))),
            tags: issue.tags.split(' / '),
            message: issue.message,
            quickFixAvailable: issue.quickFixAvailable,
            effort: issue.effort,
        };
    });
});
</script>