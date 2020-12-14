
import com.amazon.ask.Skill;
import com.amazon.ask.builder.CustomSkillBuilder;
import com.amazon.ask.SkillStreamHandler;
import handlers.*;

/**
 * Entry point for AWS Lambda
 */
public class MainStreamHandler extends SkillStreamHandler {
//    private static final Logger logger = LoggerFactory.getLogger(EverythingIntentHandler.class);

    private static Skill getSkill() {

        return new CustomSkillBuilder()
                .withSkillId("amzn1.ask.skill.70c90c47-8c77-48aa-b57c-2eb6ffdf495c")
                .addRequestHandlers(
                        new EverythingIntentHandler(),  // intent for audio adventure
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
