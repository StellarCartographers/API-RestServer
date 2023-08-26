package space.tscg.operation;

import lombok.AllArgsConstructor;
import lombok.Value;
import space.tscg.common.UpdatedValues;

@AllArgsConstructor
@Value
public class UpdatedOperation
{
    private String uuid;
    private UpdatedValues updatedValues;
}
