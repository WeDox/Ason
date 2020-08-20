# Ason
基于原生的JSONObject+JSONArray进行封装的json解析框架

#### 简单使用教程：
~~~~~~~
//解析对象
    private void parseObject(){
        String jsonDataStr = "{\"name\":\"小红\",\"age\":\"68\",\"address\": \"\"}";
        try {
            PeopleBean peopleBean = Ason.parseObject(jsonDataStr, PeopleBean.class);
            printLog(null == peopleBean ? "为空" : peopleBean.toString());
        } catch (Exception e) {
            e.printStackTrace();
            printLog("解析失败：" + e.toString());
        }
    }
    //解析数组
    private void parseObjectList(){
        String jsonDataStr = "[{\"name\":\"小红\",\"age\":\"68\",\"address\": \"\"},{\"name\":\"小鸣\",\"age\":\"18\",\"address\": \"\"}]";
        try {
            List<PeopleBean> peopleBeanList = Ason.parseObjectList(jsonDataStr, PeopleBean.class);
            printLog(null == peopleBeanList ? "为空" : peopleBeanList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            printLog("解析失败：" + e.toString());
        }
    }
    //打印
    private void printLog(String msg) {
        Log.e("ATU", null == msg ? "printLog打印的信息为空" : msg);
    }
~~~~~~~
