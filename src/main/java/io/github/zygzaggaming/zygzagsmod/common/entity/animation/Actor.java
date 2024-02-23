package io.github.zygzaggaming.zygzagsmod.common.entity.animation;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.Queue;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Actor<T extends LivingEntity & ActingEntity<T>> {
    private final T parent;
    private final Queue<AbstractAction> queuedActions = new LinkedList<>();
    private final AnimationController<T> mainAnimationController;
    private Action currentAction, nextAction;
    private @Nullable AbstractAction transientAction;
    private int timeUntilIdleAction = 5 * 20, ticksRemainingInAction = 0;

    public Actor(T parent, Action nextAction) {
        this.parent = parent;
        mainAnimationController = new AnimationController<>(parent, "main", 0, (animState) -> animState.getAnimatable().getActor().animate(animState));
        this.nextAction = this.currentAction = nextAction;
    }

    public Level level() {
        return parent.level();
    }

    public Action getNextAction() {
        return nextAction;
    }
    public Action getCurrentAction() {
        return currentAction;
    }

    public void setNextAction(Action nextAction) {
        if (!level().isClientSide()) {
            this.nextAction = nextAction;
        }
    }

    public @Nullable AbstractAction getTransientAction() {
        return transientAction;
    }

    public void setTransientAction(@Nullable AbstractAction transientAction) {
        this.transientAction = transientAction;
    }

    public AbstractAction getTopLevelAction() {
        return transientAction == null ? currentAction : transientAction;
    }

    public int getTicksRemainingInAction() {
        return ticksRemainingInAction;
    }

    public void setTicksRemainingInAction(int ticksRemainingInAction) {
        this.ticksRemainingInAction = ticksRemainingInAction;
    }

    public void tick() {
        if (parent.isIdle() && timeUntilIdleAction >= 0) timeUntilIdleAction--;
        boolean k = false;

        if (level().isClientSide()) {
            setState(parent.getEntityData().get(parent.actionStateAccessor()));
        } else {
            AbstractAction topLevelAction = getTopLevelAction();
            if (!nextAction.is(currentAction)) { // Action has been changed, queue transition
                TransitionAction transitionAction = GeneralUtil.getTransitionAnimation(currentAction, nextAction);
                if (transitionAction != null) {
                    if (k) System.out.println("Action " + currentAction + " changed to " + nextAction + "; queueing transition action");
                    queuedActions.removeIf((it) -> it instanceof TransitionAction);
                    queueAction(transitionAction);
                } else if (k) System.out.println("Action " + currentAction + " changed to " + nextAction);
            }

            // We should not be doing anything else action-related until the current action is over or can be skipped
            if (getTicksRemainingInAction() <= 0 || topLevelAction.canBeSkipped()) {
                currentAction = nextAction;
                if (k) System.out.println("Top level action " + topLevelAction + " should be over");
                // If the animation which just finished is transient, clear it
                if (topLevelAction.is(transientAction)) {
                    if (k) System.out.println("It was the currently set transient action, clearing");
                    transientAction = null;
                }

                // Check if there are any more queued transient actions
                if (!queuedActions.isEmpty()) {
                    AbstractAction transientAction = queuedActions.poll();
                    if (k) System.out.println("Queued action " + transientAction + "; playing transiently");
                    setTransientAction(transientAction);
                    setTicksRemainingInAction(transientAction.lengthInTicks() - 1);
                } else {
                    if (k) System.out.println("No queued action; playing current action " + currentAction);
                    setTicksRemainingInAction(currentAction.lengthInTicks() - 1);
                }

            } else if (getTicksRemainingInAction() > 0)
                setTicksRemainingInAction(getTicksRemainingInAction() - 1);
            if (k) System.out.println("FINAL STATE: Current action: " + currentAction + ", transient action: " + transientAction + ", next action: " + nextAction + ", queued: " + queuedActions);
            parent.getEntityData().set(parent.actionStateAccessor(), getState());
        }
    }

    public void queueAction(AbstractAction action) {
        queuedActions.add(action);
    }

    public PlayState animate(AnimationState<T> animState) {
        animState.getController().setAnimation(getTopLevelAction().raw());

        return PlayState.CONTINUE;
    }

    public void register(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(mainAnimationController);
    }

    public State getState() {
        return new State(
                nextAction,
                currentAction,
                transientAction,
                timeUntilIdleAction,
                ticksRemainingInAction,
                queuedActions
        );
    }

    public void setState(State state) {
        nextAction = state.currentAction();
        currentAction = state.lastAction();
        transientAction = state.transientAction();
        timeUntilIdleAction = state.timeUntilIdleAnimation();
        ticksRemainingInAction = state.ticksRemainingInAnimation();
        queuedActions.clear();
        queuedActions.addAll(state.queuedActions());
    }

    public boolean isTransitioning() {
        return getTransientAction() != null;
    }

    public record State(
            Action currentAction,
            Action lastAction,
            @Nullable AbstractAction transientAction,
            int timeUntilIdleAnimation,
            int ticksRemainingInAnimation,
            Queue<AbstractAction> queuedActions
    ) {
        public static State fromNetwork(FriendlyByteBuf buf) {
            Action currentAction;
            {
                ResourceLocation location = new ResourceLocation(buf.readUtf());
                currentAction = Main.actionRegistry().get(location);
                if (currentAction == null) throw new IllegalArgumentException("Invalid animation id recieved: " + location);
            }
            Action lastAction;
            {
                ResourceLocation location = new ResourceLocation(buf.readUtf());
                lastAction = Main.actionRegistry().get(location);
                if (lastAction == null) throw new IllegalArgumentException("Invalid animation id recieved: " + location);
            }
            AbstractAction transientAction;
            {
                ResourceLocation location = new ResourceLocation(buf.readUtf());
                transientAction = Main.actionRegistry().get(location);
                if (transientAction == null) transientAction = Main.transitionActionRegistry().get(location);
            }
            int timeUntilIdleAnimation = buf.readInt();
            int ticksRemainingInAnimation = buf.readInt();
            int numQueuedAnims = buf.readInt();
            Queue<AbstractAction> queuedActions = new LinkedList<>();
            for (int i = 0; i < numQueuedAnims; i++) {
                ResourceLocation location = new ResourceLocation(buf.readUtf());
                AbstractAction action = Main.actionRegistry().get(location);
                if (action == null) action = Main.transitionActionRegistry().get(location);
                queuedActions.add(action);
            }
            return new State(currentAction, lastAction, transientAction, timeUntilIdleAnimation, ticksRemainingInAnimation, queuedActions);
        }

        public void toNetwork(FriendlyByteBuf buf) {
            buf.writeUtf(currentAction.id().toString());
            buf.writeUtf(lastAction.id().toString());
            buf.writeUtf(transientAction == null ? "null" : transientAction.id().toString());
            buf.writeInt(timeUntilIdleAnimation);
            buf.writeInt(ticksRemainingInAnimation);
            buf.writeInt(queuedActions.size());
            for (AbstractAction queued : queuedActions) buf.writeUtf(queued.id().toString());
        }
    }
}
