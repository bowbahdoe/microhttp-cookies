import org.jspecify.annotations.NullMarked;

@NullMarked
module dev.mccue.microhttp.cookies {
    exports dev.mccue.microhttp.cookies;
    requires org.microhttp;
    requires static org.jspecify;
}