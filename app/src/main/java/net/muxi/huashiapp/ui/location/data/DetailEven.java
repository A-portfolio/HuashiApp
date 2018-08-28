package net.muxi.huashiapp.ui.location.data;

public class DetailEven {
    private String name;
    // search调用为false,draw route调用为true
    private boolean searchOrRoute;

    public DetailEven(String name, boolean searchOrRoute) {
        this.name = name;
        this.searchOrRoute = searchOrRoute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchOrRoute() {
        return searchOrRoute;
    }

    public void setSearchOrRoute(boolean searchOrRoute) {
        this.searchOrRoute = searchOrRoute;
    }
}
