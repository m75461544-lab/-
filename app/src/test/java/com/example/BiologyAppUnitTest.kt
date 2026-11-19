package com.example

import com.example.bio3d.Bio3DMeshBuilder
import com.example.bio3d.BioModelCatalog
import com.example.bio3d.BioModelType
import com.example.data.local.InitialQuestionBank
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BiologyAppUnitTest {

    @Test
    fun initialQuestionBank_isValidAndStrictlyChapter1Lessons1And2() {
        val questions = InitialQuestionBank.questions
        assertEquals(24, questions.size)
        assertEquals(12, questions.count { it.lesson == 1 })
        assertEquals(12, questions.count { it.lesson == 2 })

        val uniqueIds = questions.map { it.id }.toSet()
        assertEquals(questions.size, uniqueIds.size)

        for (q in questions) {
            assertTrue("Lesson must be 1 or 2", q.lesson in 1..2)
            assertTrue("Correct index out of range", q.correctIndex in 0..3)
            assertTrue("Prompt empty for ${q.id}", q.prompt.isNotBlank())
            assertTrue("Explanation empty for ${q.id}", q.explanation.isNotBlank())

            val model = BioModelType.fromId(q.modelType)
            assertEquals(q.modelType, model.id)

            q.highlightedPartId?.let { partId ->
                val validPartIds = BioModelCatalog.partsFor(model).map { it.id }.toSet()
                assertTrue("Invalid partId '$partId' on question ${q.id}", partId in validPartIds)
            }
        }
    }

    @Test
    fun bio3dMeshes_haveValidFiniteGeometryAndMatchingCatalogParts() {
        for (model in BioModelType.entries) {
            val meshParts = Bio3DMeshBuilder.buildModel(model)
            assertTrue("Mesh parts empty for ${model.id}", meshParts.isNotEmpty())

            val catalogPartIds = BioModelCatalog.partsFor(model).map { it.id }.toSet()
            val builtPartIds = meshParts.map { it.partId }.toSet()

            for (catId in catalogPartIds) {
                assertTrue("Catalog part $catId missing in 3D mesh for ${model.id}", catId in builtPartIds)
            }

            for (part in meshParts) {
                assertTrue(part.vertices.isNotEmpty())
                assertEquals(0, part.vertices.size % 6)
                val vertexCount = part.vertices.size / 6
                assertTrue("Vertex count exceeds Short limit", vertexCount <= Short.MAX_VALUE)

                for (v in part.vertices) {
                    assertTrue("Non-finite vertex coordinate in ${part.partId}", v.isFinite())
                }
                for (idx in part.indices) {
                    val intIdx = idx.toInt()
                    assertTrue(
                        "Index $intIdx out of bounds [0, $vertexCount) in ${part.partId}",
                        intIdx in 0 until vertexCount
                    )
                }
            }
        }
    }
}
