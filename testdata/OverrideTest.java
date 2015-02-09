import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;


interface ITest {
	public void testMethod(@Nonnull final String param1, @CheckForNull final String param2);
}

public class OverrideTest implements ITest {
	@Override
	@Nonnull
	public void testMethod(@Nonnull final String param1, @Nonnull final String param2) {
		//GNDN
	}
}
