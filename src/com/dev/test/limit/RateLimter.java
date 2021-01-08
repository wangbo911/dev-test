package com.dev.test.limit;import java.util.concurrent.ConcurrentHashMap;/** * @version 1.0 * @Description * @Author wangbo * @Date 2021/1/6 5:26 下午 */public class RateLimter {    private long rate;    /**     * 限流数据持久化到内存，不支持分布式限流     **/    private ConcurrentHashMap<String, Limiter> rateMap;    private static RateLimter limiter;    public RateLimter(long rate) {        this.rate = rate;    }    public static RateLimter getLimiter(long rate) {        if (limiter == null) {            return new RateLimter(rate);        }        return limiter;    }    /**     * 设置限流维度key,可以是任意维度的组合，限流器与维度解耦，缺点时限流器对象会变多，需要优化。     * 也可以将限流维度和限流起合并实现，则实现单例限流器，不用担心维度变化导致对象变多。     *     * @param key     * @return     */    public boolean limiter(String key) {        if (rateMap == null) {            rateMap = new ConcurrentHashMap<String, Limiter>();        }        boolean b = true;        if (!rateMap.containsKey(key)) {            rateMap.put(key, new Limiter(rate));        }        Limiter rateLimiter1 = rateMap.get(key);        b = rateLimiter1.acquire();        rateMap.put(key, rateLimiter1);        return b;    }}