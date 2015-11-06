package planner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created by takako.shimamoto on 15/11/05.
 */
@PlanningEntity
public class JobProcess {

    private int key;
    private int duration;
    private CronSchedule explicitSchedule;

    // Planning variables: changes during planning, between score calculations.
    private CronSchedule schedule;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public CronSchedule getExplicitSchedule() {
        return explicitSchedule;
    }

    public void setExplicitSchedule(CronSchedule explicitSchedule) {
        this.explicitSchedule = explicitSchedule;
    }

    @PlanningVariable(valueRangeProviderRefs = {"scheduleRange"})
    public CronSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(CronSchedule schedule) {
        this.schedule = schedule;
    }
}
