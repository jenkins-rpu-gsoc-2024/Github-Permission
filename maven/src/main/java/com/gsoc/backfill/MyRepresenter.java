package com.gsoc.backfill;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

class MyRepresenter extends Representer {
    
    public MyRepresenter(DumperOptions options) {
        super(options);
        this.representers.put(QuotedString.class, new RepresentQuotedString());
    }

    private class RepresentQuotedString implements Represent {
        public Node representData(Object data) {
            QuotedString str = (QuotedString) data;
            return representScalar(
                    Tag.STR, str.value, DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        }
    }
}