package com.lunch.place.entity.out;

/**
 * 改变对地方的状态，即改成喜欢或者拉黑
 */
public class PlaceState extends BasePlace {
    //喜欢的原因
    private String desc;

    @Override
    public boolean isEmpty(String... excepts) {
        return super.isEmpty(excepts) & desc != null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
