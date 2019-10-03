 public class Main {
    public static void main (String[] args) throws InvalidMoveError, InvalidParameterError {
        if (args.length <  4) {
            System.out.println("Invalid Parameters");
            System.exit(0);
        }
        // mode = "m" -> manual / "t" -> tournament
        int col = Integer.parseInt(args[0]),
            row = Integer.parseInt(args[1]),
            k = Integer.parseInt(args[2]),
            order = 0;
        String mode = args[3];
        if ("m".equals(mode) || "manual".equals(mode))
            order = Integer.parseInt(args[4]);
        GameLogic main = new GameLogic(col, row, k, mode, order);
        main.Run();
    }
}