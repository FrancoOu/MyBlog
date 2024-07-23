package org.franco.utils;

import org.franco.domain.entity.Article;
import org.franco.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {

    }
    public static <V> V copyBean(Object source, Class<V> clazz) {
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz) {

        return list.stream()
                .map(object -> copyBean(object, clazz))
                .collect(Collectors.toList());

    }
    public static void main(String[] args) {
        Article article = new Article();

        article.setId(1L);
        article.setTitle("test");

        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);

        System.out.println(hotArticleVo);

    }
}
