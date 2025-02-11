import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Links {

    private final String url;

    private List<Links> childLinks;

    public Links(String url) {
        this.url = url;
        childLinks = new ArrayList<>();
    }

    public void addChildLink(Links child){
        childLinks.add(child);
    }

}
