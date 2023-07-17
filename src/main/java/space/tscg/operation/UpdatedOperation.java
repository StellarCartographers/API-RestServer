package space.tscg.operation;

import lombok.AllArgsConstructor;
import lombok.Value;
import space.tscg.common.util.UpdatedValues;

@AllArgsConstructor
@Value
public class UpdatedOperation
{
    private String uuid;
    private UpdatedValues updatedValues;
}
