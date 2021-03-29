
import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;
import handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import watsonHandler.WatsonHandlerImpl;

/**
 * Entry point for AWS Lambda
 */
public class MainStreamHandler extends SkillStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);
    private static final Config config = new Config("/config.xml");

    private static Skill getSkill() {
        logger.info("Skill Stream Handler Initialized");

        return Skills.standard()
                .withSkillId(config.ALEXA_SKILL_ID)
                .addRequestHandlers(
                        new EverythingIntentHandler(new WatsonHandlerImpl(config)),  // intent for
                                // audio adventure
                        new HelpIntentHandler(),
                        new CancelAndStopIntentHandler(),
                        new NavigateHomeIntentHandler(),
                        new FallbackIntentHandler()
                )
                .addRequestInterceptor(new NewSessionInterceptor())
                .addResponseInterceptor(new SaveSessionInterceptor())
                // Add persistent storage support
                .withAutoCreateTable(true)
                .withTableName("AudioAdventure")
                .build();

    }

    public MainStreamHandler() {
        super(getSkill());
    }
}
