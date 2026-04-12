
import com.example.auditapi.model.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@CompoundIndexes(
    CompoundIndex(
        name = "service_version_idx",
        def = "{'serviceKey': 1, 'analysisVersion': -1}"
    )
)
@Document(collection = "sonar_analyses")
data class SonarAnalysisDocument(

    @Id
    val id: String? = null,
    val serviceKey: String,
    val repositoryKey: String? = null,
    val analysisVersion: Long,
    val projectKey: String,
    val totalIssues: Int,
    val effortTotal: Int?,

    val issues: List<SonarIssue>,
    val components: List<SonarComponent>,
    val measures: List<SonarMeasure>? = null,
    val componentTreeBase: Component? = null,
    val componentTrees: List<Component>? = null,
    val qualityGateStatus: ProjectStatus? = null
)
