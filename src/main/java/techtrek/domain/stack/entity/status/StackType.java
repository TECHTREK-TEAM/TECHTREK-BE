package techtrek.domain.stack.entity.status;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum StackType {

    REACT("react", "reactjs", "react.js"),
    VUE("vue", "vuejs", "vue.js"),
    ANGULAR("angular"),
    JQUERY("jquery"),
    HTML("html"),
    CSS("css"),
    SASS("sass"),
    TYPESCRIPT("typescript", "ts","type-script"),
    JAVASCRIPT("javascript", "js", "java-script"),
    NEXTJS("nextjs", "next.js", "next-js"),
    TAILWIND("tailwind", "tailwindcss"),

    SPRING("spring"),
    SPRING_BOOT("spring-boot"),
    DJANGO("django"),
    FLASK("flask"),
    EXPRESS("express"),
    PHP("php"),
    RUBY("ruby"),
    CPLUSPLUS("c++", "cpp", "cplusplus", "c-plusplus"),
    C("c"),
    RAILS("rails"),
    JAVA("java"),
    KOTLIN("kotlin"),
    PYTHON("python"),
    GO("go"),
    NODE("node", "node.js", "nodejs"),

    JPA("jpa"),
    MYSQL("mysql"),
    POSTGRESQL("postgresql", "postgres"),
    MONGODB("mongodb", "mongo", "mongo-db"),
    REDIS("redis"),

    GRAPHQL("graphql","graph-ql"),
    REST("rest","rest-api"),
    AWS("aws"),
    AZURE("azure"),
    DOCKER("docker"),
    KUBERNETES("kubernetes", "k8s"),
    CI_CD("ci/cd", "cicd"),
    JENKINS("jenkins"),
    GIT("git"),
    GITHUB("github","git-hub"),
    GITLAB("gitlab","git-lab"),

    DEFAULT();

    private final Set<String> aliases;

    StackType(String... aliases) {
        this.aliases = Arrays.stream(aliases)
                .map(StackType::normalize)
                .collect(Collectors.toSet());
    }

    // 모두 소문자, "-"로 바꿔줌
    public static StackType from(String raw) {
        String normalized = normalize(raw);

        return Arrays.stream(values())
                .filter(type -> type.aliases.contains(normalized))
                .findFirst()
                .orElse(DEFAULT);
    }

    private static String normalize(String value) {
        return value
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}

