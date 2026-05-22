package com.satori.dict.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author cat_y
 * @description
 * @date 2024/08/12 23:34
 */

public class DictionaryModel implements Serializable {

    private String name;

    Map<Object, Object> pairs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Object, Object> getPairs() {
        return pairs;
    }

    public void setPairs(Map<Object, Object> pairs) {
        this.pairs = pairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DictionaryModel model = (DictionaryModel) o;
        return Objects.equals(name, model.name) && Objects.equals(pairs, model.pairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pairs);
    }

    public void addDict(Object key, Object val) {
        if (null == this.pairs) {
            this.pairs = new HashMap<>();
            this.pairs.put(key, val);
        } else {
            this.pairs.put(key, val);
        }
    }


}
