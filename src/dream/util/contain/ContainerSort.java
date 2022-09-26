package dream.util.contain;

import java.util.Comparator;

public class ContainerSort implements Comparator<Containable>
{
    @Override
    public int compare(Containable one, Containable two)
    {
        if(one.isContainer() && two.isContainer())
            return one.getName().compareTo(one.getName());
        else if(one.isContainer() && !two.isContainer())
            return -1;
        else if(!one.isContainer() && two.isContainer())
            return 1;
        else if(!one.isContainer() && !two.isContainer())
            return one.getName().compareTo(two.getName());
        return 0;
    }
}
