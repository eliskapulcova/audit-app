import ageVerificationApp from '../../data/age-verification-app.json'

export default defineEventHandler((event) => {
    const project = getRouterParam(event, 'project')

    if (project === 'age-verification-app') {
        return ageVerificationApp
    }

    throw createError({
        statusCode: 404,
        statusMessage: 'Project not found'
    })
})
