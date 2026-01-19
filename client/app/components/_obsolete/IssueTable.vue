<template>
    <DataTable :value="processedIssues" responsiveLayout="scroll" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50]">
        <Column field="file" header="Location">
            <template #body="slotProps">
                <div class="flex items-center gap-2">
                    <div v-html="getIcon(slotProps.data.component).svg" class="w-5 h-5"></div>
                    <div class="flex flex-col">
                        <span>{{ slotProps.data.file }}</span>
                        <span class="text-xs text-gray-500">{{ slotProps.data.location }}</span>
                    </div>
                </div>
            </template>
        </Column>
        <Column field="rule" header="Rule">
            <template #body="slotProps">
                <a :href="slotProps.data.ruleDocLink" target="_blank">{{ slotProps.data.rule }}</a>
            </template>
        </Column>
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
import { getIcon } from 'material-file-icons';

const processedIssues = computed(() => {
    return issues.map(issue => {
        return {
            component: issue.component,
            file: `${issue.component?.match(/[^:/]*$/)?.[0] ?? ''}:${issue.line}`,
            location: issue.component?.replace(/[^:/]*$/, '').replace(/[:/]$/, '') ?? '',
            fileType:  issue.component?.match(/\.([^.]*)$/)?.[1] ?? '',
            author: issue.author,
            rule: issue.rule,
            ruleDocLink: `https://next.sonarqube.com/sonarqube/coding_rules?open=${encodeURIComponent(issue.rule)}&rule_key=${encodeURIComponent(issue.rule)}`,
            severity: issue.severity,
            type: issue.type,
            impacts: Object.fromEntries(issue.impacts.split(', ').map((impact) => impact.split('='))),
            tags: issue.tags.split(' / ').filter(tag => tag !== ''),
            message: issue.message,
            quickFixAvailable: issue.quickFixAvailable,
            effort: issue.effort,
        };
    });
});
</script>