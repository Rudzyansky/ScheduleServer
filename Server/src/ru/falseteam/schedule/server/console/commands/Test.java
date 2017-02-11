package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.server.sql.TemplateInfo;
import ru.falseteam.vframe.console.CommandAbstract;

import java.util.List;

public class Test extends CommandAbstract {
    @Override
    public void exec(CommandLine commandLine) {
        List<Template> ts = TemplateInfo.getTemplates();
        Template t = ts.get(0);
        for (int i = 0; i < 32; i++) {
            if (t.weeks.get(i)) System.out.println(i);
        }
        t.weeks.set(0, true);
        TemplateInfo.updateTemplate(t);
    }
}
