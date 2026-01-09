<template>
    <DataTable :value="issues" responsiveLayout="scroll">
        <Column field="updateDate" header="Update Date"></Column>
        <Column field="line" header="Line"></Column>
        <Column field="rule" header="Rule"></Column>
        <Column field="project" header="Project"></Column>
        <Column field="effort" header="Effort"></Column>
        <Column field="type" header="Type"></Column>
        <Column field="cleanCodeAttribute" header="Clean Code Attribute"></Column>
        <Column field="internalTags" header="Internal Tags"></Column>
        <Column field="issueStatus" header="Issue Status"></Column>
        <Column field="flows" header="Flows"></Column>
        <Column field="scope" header="Scope"></Column>
        <Column field="key" header="Key"></Column>
        <Column field="severity" header="Severity"></Column>
        <Column field="comments" header="Comments"></Column>
        <Column field="author" header="Author"></Column>
        <Column field="fromSonarQubeUpdate" header="From SonarQube Update"></Column>
        <Column field="cleanCodeAttributeCategory" header="Clean Code Attribute Category"></Column>
        <Column field="linkedTicketStatus" header="Linked Ticket Status"></Column>
        <Column field="messageFormattings" header="Message Formattings"></Column>
        <Column field="impacts" header="Impacts"></Column>
        <Column field="message" header="Message"></Column>
        <Column field="creationDate" header="Creation Date"></Column>
        <Column field="quickFixAvailable" header="Quick Fix Available"></Column>
        <Column field="tags" header="Tags"></Column>
        <Column field="codeVariants" header="Code Variants"></Column>
        <Column field="component" header="Component"></Column>
        <Column field="prioritizedRule" header="Prioritized Rule"></Column>
        <Column field="textRange" header="Text Range"></Column>
        <Column field="debt" header="Debt"></Column>
        <Column field="hash" header="Hash"></Column>
        <Column field="status" header="Status"></Column>
    </DataTable>
</template>

<script setup>
import { issues } from '~/mock/issues-report';
</script>