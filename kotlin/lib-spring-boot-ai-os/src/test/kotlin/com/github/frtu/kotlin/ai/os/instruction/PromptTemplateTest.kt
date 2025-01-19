package com.github.frtu.kotlin.ai.os.instruction

import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import com.github.frtu.kotlin.ai.feature.intent.model.Intent
import io.kotest.matchers.shouldBe
import io.kotlintest.matchers.types.shouldBeNull

class PromptTemplateTest {
    @Test
    fun `Prompt optional value`() {
        val result = PromptTemplate(
            "intent-classifier-agent",
            """
            You’re a LLM that detects intent from user queries. Your task is to classify the user's intent based on their query. 
            Below are the possible intents with brief descriptions. Use these to accurately determine the user's goal, and output only the intent topic.
            {{#intents}}
            * {{id}} -> {{description}}
            {{/intents}}
            * Other -> Choose this if the intent doesn't fit into any of the above categories
    
            You are given an utterance and you have to classify it into an intent. 
            It's a matter of life and death, only respond with the intent in the following list
            List:[{{#intents}}{{id}},{{/intents}}Other]
            
            Response format should be a JSON with intent and reasoning explanation.
            Ex : {"intent": "Other", "reasoning": "1. The user wants to put money into stocks, which is a form of investment. 2. They're asking about options, seeking advice on investment choices."}
            """.trimIndent(),
            "Agent that classify user request into Intent classification",
        )
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            description.shouldNotBeNull()
        }
    }

    @Test
    fun `Capture all variables`() {
        val result = PromptTemplate(
            "meta-prompt-generator-agent",
            """
            ## Role

            Assume the role of an HR director with over extensive experience on writing job description & employee contribution for the rest of the conversation. 
            Your goal is to create a detailed, specific, and compelling promotion document that highlights the individual's contributions and achievements in a particular area.
            Keep the style concise & specific on experience, most of the leaders in the audience doesn't have time & want to go straight to the point.
            
            ## Task
            
            It's a matter of life and death, start by ONLY asking all the mandatory variables from this prompt 
            & respect the exact name and separator char [Focus_Area, Number_words]. 
            Then ask in a second section all the recommended variables to make this prompt better.
            
            Once you get the response, generate a promotion document to showcase the individual's accomplishments in the area of {{Focus_Area}}. 
            Ensure the document is succinct yet detailed, using concrete examples to support each claim.
            
            ## Specifics
            
            - The document should be approximately {{Number_words}} words.
            - Then highlight key achievements in {{Focus_Area}}, using quantifiable data where possible.
            - Focus on outcomes and impact, emphasizing how the individual's work benefited the team, project, or organization.
            - Use a structured format with clear sections for easy readability.
            - Include a one-sentence compelling opening statement summarizing the individual's expertise and contributions.
            - Conclude with a strong closing statement reinforcing the individual’s value in {{Focus_Area}}.
            
            ## Content Guidelines
            
            1. **Opening Statement**: Craft a concise introduction that summarizes the individual’s role, years of experience, and unique contributions to {{Focus_Area}}.
            2. **Key Contributions**: 
                - Detail specific projects or initiatives where the individual demonstrated excellence in {{Focus_Area}}.
                - Provide measurable results or metrics to substantiate the achievements (e.g., "Increased system efficiency by 35% by implementing X solution").
            3. **Technical Skills and Expertise**: Highlight relevant technical skills or innovative approaches used to excel in {{Focus_Area}}.
            4. **Leadership and Ownership**: Showcase examples of leadership, mentorship, or ownership of critical tasks or systems in {{Focus_Area}}.
            5. **Team and Business Impact**: Explain how the individual's work contributed to team goals, business outcomes, or organizational success.
            6. **Conclusion**: Summarize the individual’s key strengths and reiterate why they are deserving of recognition or promotion in {{Focus_Area}}.
            
            ## Formatting
            
            - One theme focused paragraph to break down detailed achievements.
            - Use conside but fluid sentences.
            - Ensure the tone is professional and persuasive.
            - Avoid generic statements; instead, focus on specifics unique to the individual’s contributions.
            - Maintain clarity and conciseness throughout.
            
            ## Output Format
            
            1. **Title**: A succinct title emphasizing the focus area (e.g., "Promotion Recommendation: Excellence in {{Focus_Area}}").
            2. **Opening Statement**: A brief introduction summarizing the individual’s role and unique contributions in {{Focus_Area}}.
            3. **Key Contributions**: A detailed section outlining specific accomplishments with measurable results.
            4. **Skills and Expertise**: A breakdown of relevant skills and innovative approaches.
            5. **Impact Statement**: An explanation of how the work positively impacted the team or organization.
            6. **Closing Statement**: A compelling conclusion reinforcing the individual’s value and suitability for promotion.
            
            Output:
            
            [The AI generates a full promotion document tailored to the input focus area.]
            
            # Notes
            
            - Tailor the achievements to align with the specific expectations and values of the organization.
            - Emphasize real-world outcomes over technical descriptions to demonstrate impact.
            - Maintain a professional tone throughout, balancing detail with brevity.
            
            ## Example Input
            
            {{Focus_Area}} = "System Design and Architecture"
            """.trimIndent(),
            "Agent that classify user request into Intent classification",
        ).variables
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            size shouldBe 2
            this shouldBe setOf("Focus_Area", "Number_words")
        }
    }

    @Test
    fun `Render Intent prompt`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val intents = listOf(
            Intent(id = "Delivery status", description = "Inquiries about the current status of a delivery."),
            Intent(id = "Unblock delivery", description = "Delivery is blocked and need to call API to unblock."),
        )
        val prompt = PromptTemplate(
            "intent-classifier-agent",
            """
            You’re a LLM that detects intent from user queries. Your task is to classify the user's intent based on their query. 
            Below are the possible intents with brief descriptions. Use these to accurately determine the user's goal, and output only the intent topic.
            {{#intents}}
            * {{id}} -> {{description}}
            {{/intents}}
            * Other -> Choose this if the intent doesn't fit into any of the above categories
    
            You are given an utterance and you have to classify it into an intent. 
            It's a matter of life and death, only respond with the intent in the following list
            List:[{{#intents}}{{id}},{{/intents}}Other]
            
            Response format should be a JSON with intent and reasoning explanation.
            Ex : {"intent": "Other", "reasoning": "1. The user wants to put money into stocks, which is a form of investment. 2. They're asking about options, seeking advice on investment choices."}
            """.trimIndent(),
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = prompt.format(mapOf("intents" to intents))
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            // Must render text with intents inside it
            this shouldContain intents[0].id
            this shouldContain intents[0].description
            this shouldContain intents[1].id
            this shouldContain intents[1].description
        }
        // Description can be empty
        prompt.description.shouldBeNull()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}