
import com.amazon.ask.Skill;
import com.amazon.ask.builder.CustomSkillBuilder;
import com.amazon.ask.SkillStreamHandler;
import handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;

/**
 * Entry point for AWS Lambda
 */
public class MainStreamHandler extends SkillStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);
    private static final Config config = new Config("/config.xml");

    private static Skill getSkill() {
        logger.info("Skill Stream Handler Initialized");
        return new CustomSkillBuilder()
                .withSkillId(config.ALEXA_SKILL_ID)
                .addRequestHandlers(
                        new EverythingIntentHandler(config),  // intent for audio adventure
                        new HelpIntentHandler(),
                        new CancelAndStopIntentHandler(),
                        new FallbackIntentHandler(),
                        new NavigateHomeIntentHandler()
                )
                .build();

    }

    public MainStreamHandler() {
        super(getSkill());
    }
}
